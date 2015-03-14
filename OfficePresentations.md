There are basically three ways for using Office Presentations in Datasoul. They work both for Microsoft PowerPoint and OpenOffice Impress files.

# Add as Attachment #

You can add a presentation file (.pps, for instance) as an [attachment item](Attachments.md).
When the item is presented, Datasoul launches the associated application (PowerPoint or OpenOffice) just like if you have double clicked the presentation.

This method allow usage of all features and effects of the presentation application, but you won't be able to use Datasoul specific features such as [alerts](Alerts.md) or [stage output](StageOutput.md).

For more information, please refer to [attachment items](Attachments.md) documentation.

# Add as Images #

The second method consists of importing the presentation as a list of images. Each slide of the presentation will be converted into a static image and these images will be presented by Datasoul. You won't be able to use the transitions nor effects from the presentation software, but you will be able to use [alerts](Alerts.md), [stage output](StageOutput.md) and all other Datasoul features.

Before using this feature, you may need to [configure OpenOffice integration](#Configure_OpenOffice_Integration.md).

![http://wiki.datasoul.googlecode.com/git/images/convert-office-images.jpg](http://wiki.datasoul.googlecode.com/git/images/convert-office-images.jpg)

Then, add a new Image List item and use the **Add Presentation** button to select the presentation.

# Import as Song #

Before using this feature, you may need to [configure OpenOffice integration](#Configure_OpenOffice_Integration.md) too.

![http://wiki.datasoul.googlecode.com/git/images/convert-office-song.jpg](http://wiki.datasoul.googlecode.com/git/images/convert-office-song.jpg)

This option extract the text from the presentation and create a new Song in Datasoul.

Usually the  imported songs need some manual fixes. It is meant to help the migration from PowerPoint or OpenOffice to Datasoul.

For example, given this presentation it will produce the following song:

![http://wiki.datasoul.googlecode.com/git/images/convert-office-song-2.jpg](http://wiki.datasoul.googlecode.com/git/images/convert-office-song-2.jpg)
![http://wiki.datasoul.googlecode.com/git/images/convert-office-song-3.jpg](http://wiki.datasoul.googlecode.com/git/images/convert-office-song-3.jpg)

# Configure OpenOffice Integration #

Some Datasoul features rely on OpenOffice 3 to handle Office Presentation files.

In order to use these functions, please confirm that you have OpenOffice 3 installed. If need, you can download it for free from http://www.openoffice.org/.

Datasoul looks for OpenOffice in its default installation directory. If Datasoul is unable to find the OpenOffice installation you will receive an error such as:

`Error converting file: Cannot run program "soffice": CreateProcess error=2, The system cannot find the file specified`

In this case, please set in Datasoul configuration the path to OpenOffice executable, restart Datasoul to apply the changes and try again.

![http://wiki.datasoul.googlecode.com/git/images/convert-office-config.jpg](http://wiki.datasoul.googlecode.com/git/images/convert-office-config.jpg)

The default path is usually:
  * **Linux**: `/usr/bin/soffice`
  * **Windows**: `c:\Program Files\OpenOffice.org 3\program\soffice.exe`
  * **Mac**: `/Applications/OpenOffice.org.app`