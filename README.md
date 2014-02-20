medical-image-viewing
=====================
<html>
<head>
<title>Medical Image Viewing Console R1</title><base target="main">
</head>

<body>
<p>For this design project, your team will design a software system that is part of a 
medical image viewing console. Be sure to read the <a href="DesignProjectGuidelines.html">Design Project Guidelines page</a> for general information about this design project.</p>
<h2>Functional Requirements</h2>
<p>The release 1 functional requirements  are:</p>
<ol>
  <li>The system shall read medical images stored as JPEG images.&nbsp; 
  Example images are available in this
  <a href="MedImageViewerStudies.zip">zip file</a>.</li>
  <li>The system shall work with images in groupings known as <i>studies.</i>
    <ol type="A">
    <li>The images in a study shall be all image files contained within one directory.</li>
      <li>Images are ordered lexicographically by file name.</li>
      <li>The system shall display one study at a time.
        <ol type="i">
          <li>The system shall allow the user to specify the study to display.</li>
          <li>The system shall allow the user to change the study being displayed.</li>
          <li>The
system shall allow the user to optionally specify an initial study to
display when the user starts the application. If the user does not
specify an initial study, the system may prompt the user for a study to
display.</li>
          <li>The sysem shall show the user the studies available to display when the user is specifying a study to display after starting the system.</li>
        </ol>
      </li>
    </ol>
  </li>
  <li>The system shall provide display modes for viewing  images within the currently selected study.
    <ol type="A">
      <li>The system shall allow the user to view a single image from the study.
        <ol type="i">
          <li>The
user shall be able to display the next image in the study in either
forward or backward order. When displaying the first or last image, the
user shall only be able to display the next image forward or backward,
respectively, in the study.</li>
          <li>The system shall display all images in the study in the same position on the screen.</li>
        </ol>
      </li>
      <li>The system shall allow the user to view four images from the study tiled  two images by two images.
        <ol type="i">
          <li>The
user shall be able to scroll forward and backward through four images
at a time. Scrolling shall stop when the first or last set of images in
the study is displayed. If the number of images in the study is not a
multiple of four, the last display pane shall have blank areas for the
unavailable images.</li>
        </ol>
      </li>
    </ol>
  </li>
  <li>The
system shall persist the study display state. The study display state
shall include: current display mode; images currently displayed. 
    <ol type="A">
      <li>The system shall restore the  study display state when the medical image viewing system displays a study that  had its study display state  saved during a prior display of the study. The system may restore the study display state of a study on a remote imaging device, if it had been saved previously.</li>
    </ol>
  </li>
  <li>The system shall allow the user to save a study.
    <ol type="A">
      <li>The system shall save the current study display state when saving a study on the medical image viewing system. The system may save the study display state when saving a study on a remote imaging device.</li>
      <li>The system shall allow the user to save a study under a new name.
        <ol type="i">
          <li>The system shall copy  the study display state, if it has been saved, and all images in the study to the newly created study.</li>
          <li>The
study being copied can be on the medical image viewing system or on a
remote imaging device (ref. 6.B). The new copy of the study must be
stored on the medical image viewing system.</li>
        </ol>
      </li>
      <li>Images in the current study shall remain unmodified when the system saves a study with the same, or a new, name.</li>
      <li>The
system shall warn the user whenever an unsaved study display state will
be erased. The two instances when this can occur are user selection of
a new study to display, and termination of the application.</li>
    </ol>
  </li>
  <li>The system shall be able to make a network connection to a remote imaging device.
    <ol type="A">
      <li>The system shall be able to display<s> </s>a study stored on a remote imaging device.</li>
      <li>The system shall be able to copy a study from a remote imaging device to the medical image viewing system.</li>
      <li>The communication protocol is <a href="MedImageViewer-NetworkProtocol.jpg">a state-based protocol</a>
        with the following states: offline, initiating connection,
        connected-idle, listing studies (study names), listing images (study
        image file names), copying image file (one file per request), reading
      image file, terminating connection.</li>
    </ol>
  </li>
  <li>The user can exit the program and will be warned if a modified display 
  state was not saved.</li>
</ol>
<h2>Non-Functional Requirements</h2>
<p>The release 1 non-functional requirements  are:</p>
<ol>
  <li>The system shall provide either a text-based or a Gui-based interface.  Design
for both user interface mechanisms, but only implement one. &nbsp;A
text-based user interface should be able to display images according to
the functional requirements (text-based commands and status with
graphic display of images).</li>
  <li>To the extent possible the system should maintain the original intensity 
    resolution when displaying an image.</li>
  <li>The system should maintain "interactive" speeds during scrolling of images.</li>
  <li>The
system shall be written in any object-oriented&nbsp;language supported
on the standard Department of Software Engineering lab machines.
  </li><li>The
system may use code from any support packages for the chosen
implementation language that are installed on the SE lab machines. The
use of any other material must have prior instructor approval. </li><li>The defacto standard test machine shall be a machine in the  SE labs.
  </li><li>The system shall be packaged so that it can execute from a Windows Command Prompt window, i.e. outside of an IDE.
  </li>
  <li>All 
  source code must be under source code control. Evidence of source code control can be expanded keywords in the source files, or be demonstrated through a file copy of the repository log or history records.</li>
  <li>Documentation within the 
    source files will be standard file and method headers as well as 
    appropriate commentary within the source code for all non-trivial methods.&nbsp; 
    Even though methods with more than a single control structure or that are 
    longer than 5 lines may still be simple they no longer are trivial.</li>
  <li>Your design, and documentation of it, should clearly show appropriate use of design patterns.</li>
</ol>
<h2>Notes</h2>
<p>Simplifying constraints for your implementation:</p>
<ol>
  <li>You will not implement the following features:
    <ol type="A">
      <li>Network connection to a  remote imaging device. Your design should 
        show how remote studies will flow through the system.</li>
    </ol>
  </li>
  <li> All images within a study shall be the same dimensions.</li>
</ol>
<p>Remember the emphasis is on a strong design.&nbsp; The purpose of the 
  implementation is to get you to think about whether the design is indeed 
  supporting what you need to do.&nbsp; As you do your implementation you may find 
  areas where the design is not working or would not work if certain features were 
  added later.&nbsp; You may not have enough time to implement a new design.&nbsp; 
  Highlight the insights you learned about what did not work with your design.&nbsp; 
  On the flip side, also highlight any areas where the implementation was easier 
than expected because of your design.</p>
<h2>Documentation, Presentation, and Submission Instructions</h2>
<p>You can find general instructions for your 
documentation and presentation on the
<a href="DesignProjectGuidelines.html">
Design Project Guidelines page</a>. That page also has the general submission instructions. </p>
<p>For this design exercise specifically, you must 
  provide detailed sequence charts to describe the interactions between objects in 
  your system for the following operations.</p>
<ol>
<li>The loading of a study</li>
  <li>Saving of a study under a new name</li>
</ol>
<p> The team may also decide that 
there are other operations that warrant sequence charts.</p>
<hr>

</body>

</html>
