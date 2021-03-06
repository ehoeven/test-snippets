package fr.an.fssync.sortedimgfrag;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.an.fssync.model.FsEntry;
import fr.an.fssync.model.visitor.FsEntryVisitor;
import fr.an.fssync.sortedimgfrag.codec.ImageEntryFragmentIncrDataReader;

public class ImageFragmentsReader {

    public static void readImage(File imageBaseDir, String baseImageName, FsEntryVisitor imageHandler) {
	File[] imageFiles = imageBaseDir.listFiles((dir, name) -> name.startsWith(baseImageName));
	int maxHash = 0;
	Pattern pattern = Pattern.compile(baseImageName + "\\.([0-9]+)\\.[0-9]+");
	for (File imageFile : imageFiles) {
	    Matcher m = pattern.matcher(imageFile.getName());
	    if (m.matches()) {
		int hash = Integer.parseInt(m.group(1));
		maxHash = Math.max(maxHash, hash);
	    }
	}
	System.out.println("hash:" + maxHash);

	Collection<ImageEntryFragmentIncrDataReader> fragReaders = new ArrayList<>();
	for (int hash = 0; hash <= maxHash; hash++) {
	    int fragCount = 0;
	    for (int frag = 0;; frag++) {
		File hashFragFile = new File(imageBaseDir, baseImageName + "." + hash + "." + frag);
		if (!hashFragFile.exists()) {
		    break;
		}
		fragCount = Math.max(fragCount, frag + 1);
	    }
	    System.out.println("handle hash:" + hash + " => fragCount:" + fragCount);

	    for (int frag = 0; frag < fragCount; frag++) {
		File hashFragFile = new File(imageBaseDir, baseImageName + "." + hash + "." + frag);
		InputStream in;
		try {
		    in = new BufferedInputStream(new FileInputStream(hashFragFile));
		} catch (Exception ex) {
		    throw new RuntimeException("", ex);
		}
		fragReaders.add(new ImageEntryFragmentIncrDataReader(in));
	    }
	}

	System.out.println("read sorted fragments & merge all");

	try (ImageEntrySortedMergedFragmentsReader mergeReader = new ImageEntrySortedMergedFragmentsReader(
		fragReaders)) {

	    int countEntry = 0;
	    FsEntry entry;
	    while (null != (entry = mergeReader.readEntry())) {
		imageHandler.visit(entry);
		countEntry++;
	    }

	    System.out.println(".. done, read " + countEntry + " entries");
	}

    }

}
