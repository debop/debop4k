/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package debop4k.core.io;

import debop4k.core.AbstractCoreTest;
import debop4k.core.asyncs.Asyncx;
import debop4k.core.utils.Stringx;
import kotlin.Unit;
import kotlin.text.Charsets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nl.komponents.kovenant.Promise;
import org.apache.commons.io.FileUtils;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.joda.time.DateTime;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * FilexTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 9. 25.
 */
@Slf4j
public class FilexTest extends AbstractCoreTest {

  static final int LINE_COUNT = 1000;
  static final String TEST_TEXT = "동해물과 백두산이 마르고 닳도록 !!! Hello World. !@#$%^^& 안녕 세계여";
  static final byte[] TEST_BYTES = Stringx.toUtf8Bytes(Stringx.replicate(TEST_TEXT, LINE_COUNT));

  @Test
  public void createAndDeleteFile() {
    //File file = new File("./createTest.txt");
    String path = "./createTest.txt";

    Filex.deleteIfExists(path);
    try {
      File file = Filex.createFile(path);
      assertThat(Filex.exists(path)).isTrue();

      Filex.delete(file);
      assertThat(Filex.exists(path)).isFalse();
    } finally {
      Filex.deleteIfExists(path);
    }
  }

  @Test
  public void createAndDeleteDirectory() {
    String path = "./test/files";
    Filex.createDirectory(path);
    assertThat(Filex.exists(path)).isTrue();

    for (int i = 0; i < 100; i++) {
      String filePath = path + "/" + "testfile-" + i + ".txt";
      Filex.deleteIfExists(filePath);
      Filex.createFile(filePath);
    }

    Filex.deleteDirectory(path);
//    Threads.sleep(1000);
    assertThat(Filex.exists(path)).isFalse();
    Filex.deleteDirectory("./test");
  }

  @Test
  public void binaryReadWrite() {
    String path = "data.bin";
    Filex.deleteIfExists(path);

    try {
      File file = Filex.createFile(path);
      Filex.write(file, TEST_BYTES);
      byte[] readBytes = Filex.readAllBytes(path);

      assertThat(readBytes).isNotEmpty();
      assertThat(readBytes.length).isEqualTo(TEST_BYTES.length);
    } finally {
      Filex.deleteIfExists(path);
    }
  }

  @Test
  public void charReadWrite() {
    String path = "data.txt";
    Filex.deleteIfExists(path);

    List<String> lines = FastList.newList(1000);
    for (int i = 0; i < LINE_COUNT; i++) {
      lines.add(TEST_TEXT);
    }

    try {
      File file = Filex.createFile(path);
      Filex.writeLines(file, lines, true, Charsets.UTF_8);

      List<String> reads = Filex.readAllLines(path);
      assertThat(reads).isNotEmpty();
      assertThat(reads.get(0)).isEqualTo(TEST_TEXT);
      assertThat(reads.size()).isEqualTo(lines.size());
    } finally {
      Filex.deleteIfExists(path);
    }
  }

  @Test
  public void asyncBinaryReadWrite() throws Exception {
    for (int i = 0; i < 10; i++) {
      String path = "asyncData.bin";
      File file = new File(path);
      try {
        Promise<Unit, Exception> writeFuture = Filex.writeAsync(file, TEST_BYTES);
        Asyncx.ready(writeFuture);

        Promise<byte[], Exception> readFuture = Filex.readAllBytesAsync(path);

        byte[] readBytes = Asyncx.result(readFuture);
        assertThat(readBytes).isNotEmpty();
        assertThat(readBytes.length).isEqualTo(TEST_BYTES.length);

      } finally {
        Filex.deleteIfExists(path);
      }
    }
  }


  @Test
  public void bufferedStream() throws Exception {
    String path = "channel.txt";
    try {
      BufferedWriter writer = Filex.newBufferedWriter(path);
      try {
        for (int i = 0; i < 1000; i++) {
          writer.write(TEST_TEXT);
          writer.write("\n");
        }
        writer.flush();
      } finally {
        try {
          if (writer != null)
            writer.close();
        } catch (IOException ignored) {}
      }

      List<String> lines = Filex.readAllLines(path);
      assertThat(lines).isNotEmpty();
      assertThat(lines.get(0)).isEqualTo(TEST_TEXT);

    } finally {
      Filex.deleteIfExists(path);
    }
  }

  @Test
  @SneakyThrows
  public void discoverLatestFiles() {
    // 최근 3일 동안 새로 생성된 파일만 추려낸다.
    long since = DateTime.now().minusDays(5).getMillis();
    String start = ".";

    boolean filtered = false;

//    Iterator<File> iter = FileUtils.iterateFiles(new File(start), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
    Iterator<File> iter = FileUtils.iterateFiles(new File(start), new String[]{"class"}, true);
    while (iter.hasNext()) {
      File file = iter.next();
      log.trace("file={}", file.getName());
      if (!file.isDirectory()) {
        long filetime = file.lastModified();
        log.trace("filetime={}, since={}", filetime, since);
        if (filetime > since) {
          filtered = file.getCanonicalPath().contains("FilexTest");
          if (filtered)
            break;
        }
      }
    }

    assertThat(filtered).isTrue();
  }
}
