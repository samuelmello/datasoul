!define VERSION "1.2.99"

Name "Datasoul ${VERSION}"
OutFile "Datasoul-${VERSION}.exe"

# set desktop as install directory
installDir $PROGRAMFILES\Datasoul-${VERSION}

ShowInstDetails nevershow
ShowUninstDetails nevershow

Page directory
Page instfiles
UninstPage uninstConfirm
UninstPage instfiles
 
# default section start
section
 
# define output path
setOutPath $INSTDIR
 
# specify file to go in output path
file /r dist/*
file datasoul.ico

WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Datasoul-${VERSION}" \
                 "DisplayName" "Datasoul ${VERSION}"
WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Datasoul-${VERSION}" \
                 "UninstallString" "$INSTDIR\uninstaller.exe"
WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Datasoul-${VERSION}" \
                 "NoModify" 1

createShortCut "$SMPROGRAMS\Datasoul ${VERSION}.lnk" "%SystemRoot%/system32/javaw.exe" "-Xmx512m -jar datasoul.jar" $INSTDIR\datasoul.ico

# define uninstaller name
writeUninstaller $INSTDIR\uninstaller.exe
 
# default section end
sectionEnd
 
# create a section to define what the uninstaller does.
# the section will always be named "Uninstall"
section "Uninstall"
 
# Always delete uninstaller first
delete $INSTDIR\uninstaller.exe

DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Datasoul-${VERSION}" 

delete "$SMPROGRAMS\Datasoul ${VERSION}.lnk"

# now delete installed file
RMDir /r $INSTDIR
 
sectionEnd

