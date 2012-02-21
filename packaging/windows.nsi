!define VERSION "2.1-b0"

Name "Datasoul ${VERSION}"
OutFile "Datasoul-${VERSION}.exe"

; Adapted from http://nsis.sourceforge.net/FileAssoc
!macro APP_ASSOCIATE EXT FILECLASS DESCRIPTION ICON COMMAND
  WriteRegStr HKCR ".${EXT}" "" "${FILECLASS}"
  WriteRegStr HKCR "${FILECLASS}" "" `${DESCRIPTION}`
  WriteRegStr HKCR "${FILECLASS}\DefaultIcon" "" `${ICON}`
  WriteRegStr HKCR "${FILECLASS}\shell" "" "open"
  WriteRegStr HKCR "${FILECLASS}\shell\open" "" ""
  WriteRegStr HKCR "${FILECLASS}\shell\open\command" "" `${COMMAND}`
!macroend

!macro APP_UNASSOCIATE EXT FILECLASS
  DeleteRegKey HKCR `${FILECLASS}`
!macroend

; !defines for use with SHChangeNotify
!ifdef SHCNE_ASSOCCHANGED
!undef SHCNE_ASSOCCHANGED
!endif
!define SHCNE_ASSOCCHANGED 0x08000000
!ifdef SHCNF_FLUSH
!undef SHCNF_FLUSH
!endif
!define SHCNF_FLUSH        0x1000
!macro UPDATEFILEASSOC
; Using the system.dll plugin to call the SHChangeNotify Win32 API function so we
; can update the shell.
  System::Call "shell32::SHChangeNotify(i,i,i,i) (${SHCNE_ASSOCCHANGED}, ${SHCNF_FLUSH}, 0, 0)"
!macroend


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
file /r ../datasoul-src/dist/*
file /r ../native/windows/*
file datasoul.ico
file datasoul_file.ico

WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Datasoul-${VERSION}" \
                 "DisplayName" "Datasoul ${VERSION}"
WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Datasoul-${VERSION}" \
                 "UninstallString" "$INSTDIR\uninstaller.exe"
WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Datasoul-${VERSION}" \
                 "NoModify" 1

createShortCut "$SMPROGRAMS\Datasoul ${VERSION}.lnk" "%SystemRoot%/system32/javaw.exe" "-Xmx512m -jar datasoul.jar" $INSTDIR\datasoul.ico

!insertmacro APP_ASSOCIATE "servicelistz" "Datasoul.ServiceList" "Datasoul Service Plan"\
		"$\"$INSTDIR\datasoul_file.ico$\"" \
		"javaw.exe -Xmx512m -jar $\"$INSTDIR\datasoul.jar$\" $\"%1$\""

!insertmacro UPDATEFILEASSOC

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

!insertmacro APP_UNASSOCIATE "servicelistz" "Datasoul.ServiceList"

!insertmacro UPDATEFILEASSOC

# now delete installed file
RMDir /r $INSTDIR
 
sectionEnd

