/*
 * Copyright (c) 2016. KESTI co, ltd
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.core.java8.patterns.strategy;

import lombok.SneakyThrows;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPOutputStream;

/**
 * @author sunghyouk.bae@gmail.com
 */
public class Compressor {

  private final CompressionStrategy strategy;

  public Compressor(CompressionStrategy strategy) {
    this.strategy = strategy;
  }

  @SneakyThrows(IOException.class)
  public void compress(Path inFile, File outFile) {
    try (OutputStream out = new FileOutputStream(outFile)) {
      Files.copy(inFile, strategy.compress(out));
    }
  }

  public static void classBasedExample(Path inFile, File outFile) {
    Compressor gzip = new Compressor(new GzipCompressionStrategy());
    gzip.compress(inFile, outFile);

  }

  public static void lambdaBasedExample(Path inFile, File outFile) {
    Compressor gzipCompressor = new Compressor(GZIPOutputStream::new);
    gzipCompressor.compress(inFile, outFile);
  }
}
