package testspringbootprofile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import testspringbootprofile.config.AppPlaceholderService;
import testspringbootprofile.impl.MockedBean;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles({"profile1", "other"})
public class App2Profile1Test extends AbstractTest {

	static {
		System.out.println();
		System.out.println("###### app.App2Profile1Test.cinit");
	}

	@Autowired // not @MockBean !
	private MockedBean mockedBean;

	@Autowired
	private AppPlaceholderService placeholders;

	@Test
	public void testDumpPlaceholders() {
		System.out.println("### app.AppProfile1Test.testDumpPlaceholders");
		placeholders.dumpPlaceholders();
	}
	
	@Test
	public void testFoo() {
		System.out.println("### app.testFoo");
		mockedBean.foo();
	}

}
