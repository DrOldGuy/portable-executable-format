// Copyright (c) 2023 Goosebump Designs LLC

package com.goosebumpdesigns.pe;

import java.util.TreeMap;
import com.goosebumpdesigns.pe.file.SectionBuffer;
import com.goosebumpdesigns.pe.file.SectionTableBuffer;

/**
 * This class represents the section header table. It is itself a Map that maps the section name to
 * the section data.
 */
@SuppressWarnings("serial")
public class PESectionTable extends TreeMap<String, PESection> {

  /**
   * Load the section header data into {@link PESection} objects and add them to the map in this
   * object.
   * 
   * @param sectionTableBuffer The section table buffer.
   */
  public PESectionTable(SectionTableBuffer sectionTableBuffer) {
    for(SectionBuffer buffer : sectionTableBuffer) {
      PESection section = new PESection(buffer);
      put(section.getName(), section);
    }
  }

}
