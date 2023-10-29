# portable-executable-format
A Java project that reads and parses a Windows Portable Executable (DLL or .exe file).

This does not completely parse the entire PE file. Currently the following is supported:
* Common header
* Optional header (PE32 and PE32+ formats)
* Optional header data directories
* Section headers
* Exports section

If you need additional PE support, please fork this repo or create a pull request.

This project was created based on this [documentation](https://learn.microsoft.com/en-us/windows/win32/debug/pe-format).

## Usage
The easiest way to use this library is to call goosebump.pe.builder.PEFileBuilder.newPEFile. Pass the file name as a Path object. This method creates and returns a goosebump.pe.PEFile object with all data loaded.

##Caveats
The parser has been nominally tested with Windows 10 PE Dynamic Link Libraries (DLLs). It may not support older or newer versions.

## Issues
Please feel free to submit a pull request!
