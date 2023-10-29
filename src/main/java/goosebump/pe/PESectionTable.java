// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.pe;

import java.util.TreeMap;
import goosebump.pe.file.SectionBuffer;
import goosebump.pe.file.SectionTableBuffer;

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
