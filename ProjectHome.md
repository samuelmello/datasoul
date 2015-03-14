Datasoul is an open source presentation software to display lyrics and text in churches services.

![http://wiki.datasoul.googlecode.com/git/images/main-window.jpg](http://wiki.datasoul.googlecode.com/git/images/main-window.jpg)

You can check the [Screenshots](Screenshots.md) and find more information in our [wiki](http://code.google.com/p/datasoul/wiki/Overview?tm=6)

Check [what's new](WhatsNew.md) in the new version.

### Presentation Features ###
  * Songs, texts, images and videos
  * Flexible template system to define how the items are formatted
  * Video background (both video file and live from camera)
  * Quick display of alerts (useful for nursery or parking announcements, for instance)
  * Supports any display resolution (including widescreen)
  * Launch any external file, such as presentations made in other applications (PowerPoint, OpenOffice, Adobe Flash, etc)
  * Use a different output for  the pastor or musicians, including additional information such as the next slide or a clock
  * Export presentation to [PDF file](ExportPDF#Export_Slides_in_PDF.md)
  * [Import Office Presentations](OfficePresentations.md), from both PowerPoint and OpenOffice
  * [Remote Display](RemoteDisplay.md) over network
### Service Management Features ###
  * Insert Bible texts using Sword
  * Song Management
    * Handle both lyrics and chords
    * Chords transposition
    * Guitar tabs
  * [Create printouts](ExportPDF#Create_Service_Printouts.md) for the service, containing all songs (including chords and guitar tabs)
  * Easily sync database among several computers
  * [Publish your service plan online](PublishOnline.md) and share with your friends
### Import Tools ###
  * [Tool available](ImportEasyWorship.md) to import songs from EasyWorship
  * [Import Office Presentations](OfficePresentations.md), from both PowerPoint and OpenOffice



---


## Download and Installation ##

Datasoul runs on Windows, Linux and Mac.

> ### ![http://www.gstatic.com/codesite/ph/images/dl_arrow.gif](http://www.gstatic.com/codesite/ph/images/dl_arrow.gif) [Download Datasoul](DownloadInstall.md) ###


---


## Development Information ##

You can find basic information [here](Development.md) and contact us on our [development group](http://groups.google.com/group/datasoul-devel/).


---


## Public Beta 2.1 ##

We are working towards Datasoul 2.1.

The major change is the replacement of gstreamer-java by vlcj. Advantages include:
  * Better performance for video and video background (We've run Full HD video as background in a AMD Athlon X2)
  * Easier installation, as all libraries are shipped with installation package. No more need to install gstreamer separately for Windows users
  * MacOS support out of the box


It should run under:
  * Windows with updated Java. Highly encouraged Java 7
  * Linux with vlc 2.0 and Java 7 (mandatory)
  * MacOS 10.7


The public beta is a pre-release version for testing. **It probably contain bugs and should NOT be used in production**. Please feel free to download, test and report success/errors to us

> ### ![http://www.gstatic.com/codesite/ph/images/dl_arrow.gif](http://www.gstatic.com/codesite/ph/images/dl_arrow.gif) [Download Datasoul 2.1 Beta](http://code.google.com/p/datasoul/downloads/list?q=label:Beta) ###
