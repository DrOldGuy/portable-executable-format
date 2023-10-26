// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.portable.executable.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import lombok.Getter;
import lombok.ToString;

/**
 * This class is an implementation of a random access file that only allows reading.
 */
@ToString
public class PEReader extends RandomAccessFile {
  private static final String READ = "r";

  @Getter
  private Path path;

  /**
   * @param file
   * @param mode
   * @throws FileNotFoundException
   */
  public PEReader(Path path) throws IOException {
    super(path.toFile(), READ);

    Path realPath;

    try {
      realPath = path.toRealPath();
    } catch (IOException e) {
      realPath = path;
    }

    this.path = realPath;
  }

  /**
   * Create and populate a buffer of the given length read from the given offset.
   * 
   * @param offset The offset to start reading.
   * @param length The length to read.
   * @return The populated buffer.
   * @throws IOException
   */
  public byte[] readBytes(long offset, int length) throws IOException {
    byte[] buffer = new byte[length];

    seek(offset);
    readFully(buffer);

    return buffer;
  }

  @Override
  public int skipBytes(int n) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void write(int b) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void write(byte[] b) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public long getFilePointer() throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setLength(long newLength) throws IOException {
    throw new UnsupportedOperationException();
  }

  /**
   * @param peSignatureLocation
   * @return
   * @throws IOException
   */
  public int readUnsignedByteAt(long position) throws IOException {
    seek(position);
    return readUnsignedByte();
  }

}
