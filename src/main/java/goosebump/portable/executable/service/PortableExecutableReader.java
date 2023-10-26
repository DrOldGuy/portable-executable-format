// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.portable.executable.service;

import java.nio.file.Path;
import java.util.Objects;
import goosebump.portable.executable.model.pe.PEFile;

/**
 * This class represents a DLL file in the local file path. The file is not read and parsed until an
 * information method (i.e., "{@link #exports}") is called. The DLL is read entirely into memory,
 * pertinent data is extracted, and the file contents are discarded. The constructor will not throw
 * an exception, but any method called that causes the file to be read may throw an exception.
 */
public class PortableExecutableReader {

  private Path path;
  // private int peHeaderOffset;
  // private boolean bigEndian = true; // C writes little-endian, Java is big-endian
  private PEFile peFile;

  /**
   * Create a new DLL reader object.
   * 
   * @param dllPath
   */
  public PortableExecutableReader(Path dllPath) {
    this.path = dllPath;
  }

  private synchronized void loadPEFile() {
    if (Objects.isNull(peFile)) {
      peFile = new PEFile(path);
    }
  }
}
