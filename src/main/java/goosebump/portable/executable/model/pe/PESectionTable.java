// Copyright (c) 2023 Goosebump Designs LLC

package goosebump.portable.executable.model.pe;

import static goosebump.portable.executable.Constants.PE_SECTION_SIZE;
import java.util.TreeMap;
import goosebump.portable.executable.file.ByteOrderBuffer;

/**
 * 
 */
@SuppressWarnings("serial")
public class PESectionTable extends TreeMap<String, PESection> {

  /**
   * @param sectionTableBuffer
   */
  public PESectionTable(ByteOrderBuffer sectionTableBuffer, int numSections) {
    int sectionOffset = 0;
    
    for(int sectionNo = 0; sectionNo < numSections; sectionNo++) {
      PESection section = new PESection(sectionTableBuffer, sectionOffset);
      put(section.getName(), section);
      
      sectionOffset += PE_SECTION_SIZE;
    }
  }

}
