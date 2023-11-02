// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe.file;

import java.util.Iterator;

/**
 * This class iterates over the section header table buffer, providing sub-buffers for each section.
 * It allows you to do this:
 * 
 * <pre>
 * <code>
 * byte[] sectionTableBytes = (load section table header byte array)
 * ByteOrderBuffer byteOrderBuffer = new ByteOrderBuffer(sectionTableBytes, byteOrder);
 * SectionTableBuffer sectionTableBuffer = new SectionTableBuffer(byteOrderBuffer, numSections, sectionSize);
 * 
 * for(SectionBuffer : sectionTableBuffer) {
 *   // do something here...
 * }
 * </code>
 * </pre>
 */
public class SectionTableBuffer implements Iterable<SectionBuffer> {
  private ByteOrderBuffer sectionTableBuffer;
  int sectionSize;
  int numberOfSections;

  /**
   * Create the section table buffer.
   * 
   * @param sectionTableBuffer The {@link ByteOrderBuffer} that contains the section header data for
   *        all sections.
   * @param numberOfSections The number of sections.
   * @param sectionSize The size of each section.
   */
  public SectionTableBuffer(ByteOrderBuffer sectionTableBuffer, int numberOfSections,
      int sectionSize) {
    this.sectionTableBuffer = sectionTableBuffer;
    this.sectionSize = sectionSize;
    this.numberOfSections = numberOfSections;
  }

  /**
   * This returns the section buffer iterator. It can be used in an enhanced for loop as described
   * in the class overview.
   */
  @Override
  public Iterator<SectionBuffer> iterator() {
    return new SectionIterator(sectionTableBuffer, numberOfSections, sectionSize);
  }

  /**
   * This is the iterator. It pulls off a chunk of the buffer with each iteration.
   */
  static class SectionIterator implements Iterator<SectionBuffer> {
    int sectionSize;
    int numberOfSections;
    int curSection;
    ByteOrderBuffer sectionTableBuffer;

    /**
     * @param numberOfSections
     * @param sectionSize
     */
    public SectionIterator(ByteOrderBuffer sectionTableBuffer, int numberOfSections,
        int sectionSize) {
      this.sectionSize = sectionSize;
      this.numberOfSections = numberOfSections;
      this.sectionTableBuffer = sectionTableBuffer;
    }

    /**
     * Returns {@code true} if there are more sections.
     */
    @Override
    public boolean hasNext() {
      return curSection < numberOfSections;
    }

    /**
     * Returns the next section buffer.
     */
    @Override
    public SectionBuffer next() {
      byte[] bytes = sectionTableBuffer.getBytes(curSection++ * sectionSize, sectionSize);
      SectionBuffer buf = new SectionBuffer(bytes, sectionTableBuffer.getByteOrder());
      return buf;
    }

  }

}
