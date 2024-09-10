import java.util.function.Consumer;
import java.util.function.Supplier;

import edu.uwm.cs351.Ball;
import edu.uwm.cs351.BallSeq;
import junit.framework.TestCase;


public class TestInvariant extends TestCase {
	protected BallSeq.Spy spy;
	protected int reports;
	protected BallSeq r;
	
	protected void assertReporting(boolean expected, Supplier<Boolean> test) {
		reports = 0;
		Consumer<String> savedReporter = spy.getReporter();
		try {
			spy.setReporter((String message) -> {
				++reports;
				if (message == null || message.trim().isEmpty()) {
					assertFalse("Uninformative report is not acceptable", true);
				}
				if (expected) {
					assertFalse("Reported error incorrectly: " + message, true);
				}
			});
			assertEquals(expected, test.get().booleanValue());
			if (!expected) {
				assertEquals("Expected exactly one invariant error to be reported", 1, reports);
			}
			spy.setReporter(null);
		} finally {
			spy.setReporter(savedReporter);
		}
	}
	
	protected void assertWellFormed(boolean expected, BallSeq r) {
		assertReporting(expected, () -> spy.wellFormed(r));
	}

	@Override // implementation
	protected void setUp() {
		spy = new BallSeq.Spy();
	}

	public void test0() {
		r = spy.newInstance(null, 0, 0);
		assertWellFormed(false, r);
	}
	
	public void test1() {
		r = spy.newInstance(new Ball[0], 0, 0);
		assertWellFormed(true, r);
	}
	
	public void test2() {
		r = spy.newInstance(new Ball[3], -1, 0);
		assertWellFormed(false, r);
	}
	
	public void test3() {
		r = spy.newInstance(new Ball[3],4,0);
		assertWellFormed(false, r);
	}
	
	public void test4() {
		r = spy.newInstance(new Ball[4],4,0);
		assertWellFormed(true, r);
	}
	
	public void test5() {
		r = spy.newInstance(new Ball[10],0,0);
		assertWellFormed(true, r);
	}
	
	public void test6() {
		r = spy.newInstance(new Ball[5],4,-1);
		assertWellFormed(false, r);
	}
	
	public void test7() {
		r = spy.newInstance(new Ball[3],3,3);
		assertWellFormed(true, r);
	}
	
	public void test8() {
		r = spy.newInstance(new Ball[5],3,4);
		assertWellFormed(false, r);
	}
	
	public void test9() {
		r = spy.newInstance(new Ball[5],4,4);
		assertWellFormed(true, r);
	}

}
