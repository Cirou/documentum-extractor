# documentum-extractor

This is a java-based documentum quick doc extractor

This doesn't use the getFile API that sometimees can be very slow
Instead it takes the dmr_content information related to the document and directly transfer the file from the docbase to the desired destination folder

# Pre-reqs
- Install [JDK](https://www.oracle.com/it/java/technologies/javase/javase-jdk8-downloads.html) (this was based on JDK 8)
- Install [Eclipse](https://code.visualstudio.com/) (or your favorite IDE)

# Getting started
- Clone the repository
```
git clone --depth=1 https://github.com/Cirou/documentum-extractor.git <your_project_name>
```
- Import the project in your IDE
- Insert docbase data into the dfc.conf and extractor.conf
- Extract the r_object_id list of the document you want to download and put it into the documentsToExtract.txt
- Execute the DocumentExtractor.java as a Java Application

# License
The MIT License (MIT)

Copyright (c) 2018 Ciro Aiello

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
