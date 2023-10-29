// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.pe.file;

import java.util.Iterator;

/**
 * 
 */
public class SectionTableBuffer implements Iterable<SectionBuffer> {
  private ByteOrderBuffer sectionTableBuffer;
  int sectionSize;
  int numberOfSections;

  /**
   * Create the section table buffer.
   * 
   * @param sectionTableBuffer
   * @param numberOfSections
   * @param sectionSize
   */
  public SectionTableBuffer(ByteOrderBuffer sectionTableBuffer, int numberOfSections,
      int sectionSize) {
    this.sectionTableBuffer = sectionTableBuffer;
    this.sectionSize = sectionSize;
    this.numberOfSections = numberOfSections;
  }

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
      byte[] bytes = sectionTableBuffer.bytes(curSection++ * sectionSize, sectionSize);
      SectionBuffer buf = new SectionBuffer(bytes, sectionTableBuffer.getByteOrder());
      return buf;
    }

  }

}
