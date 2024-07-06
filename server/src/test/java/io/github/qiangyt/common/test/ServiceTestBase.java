package io.github.qiangyt.common.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import org.opentest4j.AssertionFailedError;

import io.github.qiangyt.common.err.BaseError;

import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.BeforeEach;


/**
 * Base class for Service unit tests, encapsulating basic usage methods for Mockito.
 *
 * The purpose of Service unit testing is to verify the correctness of service logic,
 * including parameter/result passing with DAOs.
 */

@Disabled
public abstract class ServiceTestBase {


  @BeforeEach
  public void beforeEach() {
    MockitoAnnotations.openMocks(this);
  }

  public static boolean matchBoolean(boolean that) {
    return ArgumentMatchers.booleanThat(b -> {
      Assertions.assertEquals(Boolean.valueOf(that), b);
      return true;
    });
  }

  public static String matchString(String that) {
    return ArgumentMatchers.argThat((String actual) -> that.equals(actual));
  }

  public static int matchInt(int that) {
    return ArgumentMatchers.intThat(i -> {
      Assertions.assertEquals(Integer.valueOf(that), i);
      return true;
    });
  }

  @SuppressWarnings("unchecked")
  public static <T extends BaseError> T assertThrows (Class<T> expectedType, Enum<?> code, Executable executable) {
      try {
        executable.execute();
      } catch (Throwable actualException) {
        if (expectedType.isInstance(actualException)) {
          return (T) actualException;
        }

        // UnrecoverableExceptions.rethrowIfUnrecoverable(actualException);

        throw new AssertionFailedError("caught unexpected exception", expectedType, actualException.getClass(), actualException);
      }

      throw new AssertionFailedError("no expected exception throw", expectedType, "null");
    }

}
