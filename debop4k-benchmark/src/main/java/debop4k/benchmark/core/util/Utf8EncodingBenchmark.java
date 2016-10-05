package debop4k.benchmark.core.util;

import debop4k.core.utils.codecs.Utf8StringEncoder;
import org.openjdk.jmh.annotations.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * NOTE: 벤치마킹 테스트 할 때만 수행하세요.
 */
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
public class Utf8EncodingBenchmark {
  // experiment test input
  private List<String> strings = new ArrayList<String>();

  // CharsetEncoder helper buffers
  private char[] chars;
  private CharBuffer charBuffer;
  private CharsetEncoder encoder;

  // My own encoder
  private Utf8StringEncoder customEncoder;

  // Destination buffer, the slayer
  private ByteBuffer buffySummers;

  @Setup
  public void init() {
    boolean useDirectBuffer = true; // Boolean.getBoolean("Utf8EncodingBenchmark.directBuffer");
    InputStream testTextStream = null;
    InputStreamReader inStreamReader = null;
    BufferedReader buffReader = null;
    try {
      testTextStream = getClass().getResourceAsStream("/Utf8Samples.txt");
      inStreamReader = new InputStreamReader(testTextStream, "UTF-8");
      buffReader = new BufferedReader(inStreamReader);
      String line;
      while ((line = buffReader.readLine()) != null) {
        strings.add(line);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      closeStream(testTextStream);
      closeReader(inStreamReader);
      closeReader(buffReader);
    }

    if (useDirectBuffer) {
      buffySummers = ByteBuffer.allocateDirect(4096);
    } else {
      buffySummers = ByteBuffer.allocate(4096);
    }
    chars = new char[4096];
    charBuffer = CharBuffer.wrap(chars);
    encoder = Charset.forName("UTF-8").newEncoder();
    customEncoder = new Utf8StringEncoder();
  }

  private void closeStream(InputStream inStream) {
    if (inStream != null) {
      try {
        inStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void closeReader(Reader buffReader) {
    if (buffReader != null) {
      try {
        buffReader.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Benchmark
  public int customEncoder() {
    int countBytes = 0;
    for (String string : strings) {
      buffySummers.put(customEncoder.decode(string));
      countBytes += buffySummers.position();
      buffySummers.clear();
    }
    return countBytes;
  }

  @Benchmark
  public int stringGetBytes() throws UnsupportedEncodingException {
    int countBytes = 0;
    for (String string : strings) {
      buffySummers.put(string.getBytes("UTF-8"));
      countBytes += buffySummers.position();
      buffySummers.clear();
    }
    return countBytes;
  }

  @Benchmark
  public int charsetEncoder() throws UnsupportedEncodingException {
    int countBytes = 0;
    for (String source : strings) {
      int length = source.length();
      source.getChars(0, length, chars, 0);
      charBuffer.position(0);
      charBuffer.limit(length);
      encoder.reset();
      encoder.encode(charBuffer, buffySummers, true);
      countBytes += buffySummers.position();
      buffySummers.clear();
    }
    return countBytes;
  }
}