package fr.an.fssync.sortedimgfrag;

import java.io.Closeable;
import java.util.Collection;
import java.util.TreeMap;

import fr.an.fssync.model.FsEntry;
import fr.an.fssync.sortedimgfrag.codec.ImageEntryFragmentIncrDataReader;
import lombok.val;

public class ImageEntrySortedMergedFragmentsReader implements Closeable {

    private TreeMap<FsEntry, ImageEntryFragmentIncrDataReader> currEntry2Iterator = new TreeMap<>();

    public ImageEntrySortedMergedFragmentsReader(Collection<ImageEntryFragmentIncrDataReader> fragReaders) {
	for (ImageEntryFragmentIncrDataReader fragReader : fragReaders) {
	    FsEntry entry = fragReader.readEntry();
	    if (entry != null) {
		currEntry2Iterator.put(entry, fragReader);
	    } else {
		fragReader.close();
	    }
	}
    }

    @Override
    public void close() {
	if (!currEntry2Iterator.isEmpty()) {
	    System.out.println("merge reader not fully read => explicit finish close..");
	    for (val reader : currEntry2Iterator.values()) {
		reader.close();
	    }
	    currEntry2Iterator.clear();
	}
    }

    public FsEntry readEntry() {
	if (currEntry2Iterator.isEmpty()) {
	    return null;
	}

	val firstEntry = currEntry2Iterator.firstEntry();
	FsEntry imageEntry = firstEntry.getKey();
	ImageEntryFragmentIncrDataReader fragReader = firstEntry.getValue();

	currEntry2Iterator.remove(imageEntry);
	// read next
	FsEntry nextEntry = fragReader.readEntry();
	if (nextEntry != null) {
	    currEntry2Iterator.put(nextEntry, fragReader);
	} else {
	    fragReader.close();
	}

	return imageEntry;
    }

}
