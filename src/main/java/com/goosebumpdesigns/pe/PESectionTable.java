// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe;

import java.util.TreeMap;
import com.goosebumpdesigns.pe.file.SectionBuffer;
import com.goosebumpdesigns.pe.file.SectionTableBuffer;

/**
 * 
 */
@SuppressWarnings("serial")
public class PESectionTable extends TreeMap<String, PESection> {

  /**
   * @param sectionTableBuffer
   */
  public PESectionTable(SectionTableBuffer sectionTableBuffer) {
    for(SectionBuffer buffer : sectionTableBuffer) {
      PESection section = new PESection(buffer);
      put(section.getName(), section);
    }
  }

}
