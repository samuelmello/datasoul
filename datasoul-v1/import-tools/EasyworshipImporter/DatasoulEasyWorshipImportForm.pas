unit DatasoulEasyWorshipImportForm;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, DB, DBTables, StdCtrls, Grids, DBGrids, ComCtrls, jpeg, ExtCtrls,
  Filectrl, ShlObj;

type
  TfrmDatasoulEasyWorshipImportForm = class(TForm)
    btnGo: TButton;
    Table1: TTable;
    Table1Title: TStringField;
    Table1Author: TStringField;
    Table1RecID: TIntegerField;
    Table1TextPercentageBottom: TSmallintField;
    Table1Copyright: TStringField;
    Table1Administrator: TStringField;
    Table1Words: TMemoField;
    Table1DefaultBackground: TBooleanField;
    Table1BKType: TSmallintField;
    Table1BKColor: TIntegerField;
    Table1BKGradientColor1: TIntegerField;
    Table1BKGradientColor2: TIntegerField;
    Table1BKGradientShading: TSmallintField;
    Table1BKGradientVariant: TSmallintField;
    Table1BKTexture: TSmallintField;
    Table1BKBitmapName: TStringField;
    Table1BKBitmap: TBlobField;
    Table1BKAspectRatio: TSmallintField;
    Table1Favorite: TSmallintField;
    Table1LastModified: TDateTimeField;
    Table1DemoData: TBooleanField;
    Table1SongNumber: TStringField;
    Table1BKThumbnail: TBlobField;
    Table1OverrideEnabled: TBooleanField;
    Table1FontSizeLimitDefault: TBooleanField;
    Table1FontSizeLimit: TSmallintField;
    Table1FontNameDefault: TBooleanField;
    Table1FontName: TStringField;
    Table1TextColorDefault: TBooleanField;
    Table1TextColor: TIntegerField;
    Table1ShadowColorDefault: TBooleanField;
    Table1ShadowColor: TIntegerField;
    Table1OutlineColorDefault: TBooleanField;
    Table1OutlineColor: TIntegerField;
    Table1ShadowText: TSmallintField;
    Table1OutlineText: TSmallintField;
    Table1BoldText: TSmallintField;
    Table1ItalicText: TSmallintField;
    Table1TextAlignment: TSmallintField;
    Table1VertAlignment: TSmallintField;
    Table1TextPercentRectDefault: TBooleanField;
    Table1TextPercentageLeft: TSmallintField;
    Table1TextPercentageTop: TSmallintField;
    Table1TextPercentageRight: TSmallintField;
    Table1VendorID: TIntegerField;
    RichEdit1: TRichEdit;
    Image1: TImage;
    Label1: TLabel;
    Label2: TLabel;
    lblEasyWorship: TLabel;
    Label4: TLabel;
    lblDatasoul: TLabel;
    btnClose: TButton;
    ProgressBar: TProgressBar;
    btnChooseEasyWorship: TButton;
    btnChooseDatasoul: TButton;
    lblDatabaseSize: TLabel;
    Shape1: TShape;
    lblDone: TLabel;
    procedure btnGoClick(Sender: TObject);
    procedure btnCloseClick(Sender: TObject);
    procedure btnChooseEasyWorshipClick(Sender: TObject);
    procedure FormCreate(Sender: TObject);
    procedure btnChooseDatasoulClick(Sender: TObject);
  private
    { Private declarations }
    TemplateList: TStringList;
    OverwriteMode : Integer;
    ConvertedCount : Integer;
    function ConvertLyrics(Words: String): String;
    function ConvertTitle(Title: String): String;
    function CheckEasyworshipPath(Path: String): Boolean;
    function CheckDatasoulPath(Path: String): Boolean;
    procedure CreateDatasoulSong(Title, Author, Copyright, Lyrics: String);
    procedure UpdateGoButton;
  public
    { Public declarations }
  end;

var
  frmDatasoulEasyWorshipImportForm: TfrmDatasoulEasyWorshipImportForm;

implementation

{$R *.dfm}

(**
 * Convert a song lyrics from EasyWorship to Datasoul
 * EasyWorship stores it in RTF and uses a blank line to split slides
 * and lines Containing 'Verse [0-9]+' or 'Chorus [0-9]+' to split verses
 * We use a RichEdit control to strip the RTF formating and then replace
 * these lines with Datasoul's '==' and '==='
 *)
function TfrmDatasoulEasyWorshipImportForm.ConvertLyrics(Words: String): String;
var
  lines : TStrings;
  linesconv : TStringList;
  i : Integer;
  nextbreak : Boolean;
  s : String;
begin
  RichEdit1.Lines.Clear;

  RichEdit1.PlainText := False;
  RichEdit1.Text := Words;
  RichEdit1.PlainText := True;

  lines := RichEdit1.Lines;
  linesconv := TStringList.Create;

  nextbreak := True;
  for I := 0 to lines.Count - 1 do
    begin
      s := LowerCase(Trim(lines.Strings[i]));
      if s = '' then
        nextbreak := True
      else
        begin
          if nextbreak then
            begin
              nextbreak := False;
              if (Pos('verse', s) = 1) or (Pos('chorus', s) = 1) or (Pos('slide', s) = 1)  or (Pos('bridge', s) = 1) then
                begin
                  if i > 0 then
                    linesconv.Add('===');
                end
              else
                begin
                  linesconv.Add('==');
                  linesconv.Add(lines.Strings[i]);
                end;
            end
          else
            linesconv.Add(lines.Strings[i]);
        end;

    end;
  Result := linesconv.Text;
end;

procedure TfrmDatasoulEasyWorshipImportForm.UpdateGoButton;
begin
  if (lblEasyWorship.Caption <> '') and (lblDatasoul.Caption <> '') then
    btnGo.Enabled := True
  else
    btnGo.Enabled := False;
end;

procedure TfrmDatasoulEasyWorshipImportForm.FormCreate(Sender: TObject);
var
   r: Bool;
   path: array[0..Max_Path] of Char;
   tplFilePath, tmp : String;
   tplFile : TextFile;
begin
    OverwriteMode := -1;
    ConvertedCount := 0;

   // Load the template
   tplFilePath := ExtractFilePath( ParamStr(0) ) + '\DatasoulSong.dstpl';
   if not FileExists(tplFilePath) then
     begin
       ShowMessage('Fatal Error: Unable to find DatasoulSong.dstpl file.');
       Application.Terminate;
     end
   else
    begin
      AssignFile(tplFile, tplFilePath);
      Reset(tplFile);
      TemplateList := TStringList.Create;
      while not EOF(tplFile) do
        begin
          ReadLn(tplFile, tmp);
          TemplateList.Add(tmp);
        end;
      CloseFile(tplFile);
    end;

   // Guess EasyWorship Directory
   r := ShGetSpecialFolderPath(0, path, $002E, False) ;
   if r then
     CheckEasyworshipPath(path+'\Softouch\EasyWorship\Default\Databases\Data');

   // Guess Datasoul Directory
   r := ShGetSpecialFolderPath(0, path, CSIDL_PROFILE, False) ;
   if r then
     CheckDatasoulPath(path+'\.datasoul\data');

end;

(**
 * Try to open EasyWorship Database.
 * We baslically search for a table called "Songs.DB' in Paradox format
 *)
function TfrmDatasoulEasyWorshipImportForm.CheckEasyworshipPath(Path: String): Boolean;
var
  TablePath : String;
begin
  TablePath := Path + '\Songs.DB';
  if FileExists(TablePath) then
    begin
      try
         Table1.Active := False;
         Table1.TableName := TablePath;
         Table1.Active := True;
         lblEasyWorship.Caption := Path;
         Result := True;
      except
         Table1.Active := False;
         Result := False
      end;
    end
  else
    Result := False;

  if Result then
    lblDatabaseSize.Caption := 'Found '+IntToStr(Table1.RecordCount)+' songs'
  else
    lblDatabaseSize.Caption := '';

  UpdateGoButton;
end;

(**
 * Verify that the given directory contains Datasoul files
 *)
function TfrmDatasoulEasyWorshipImportForm.CheckDatasoulPath(Path: String): Boolean;
begin
  if DirectoryExists(Path+'\Songs') and DirectoryExists(Path+'\ServiceLists') and DirectoryExists(Path+'\Templates') then
    begin
         lblDatasoul.Caption := Path;
         Result := True;
    end
  else
    Result := False;

  UpdateGoButton;
end;

procedure TfrmDatasoulEasyWorshipImportForm.btnChooseDatasoulClick(
  Sender: TObject);
var
  Dir: String;
begin
  Dir := lblDatasoul.Caption;
  SelectDirectory('Select Datasoul Songs Directory', '', Dir);
  if not CheckDatasoulPath(Dir) then
    ShowMessage('The selected directory does not contain a valid Datasoul Data Directory');

end;

procedure TfrmDatasoulEasyWorshipImportForm.btnChooseEasyWorshipClick(
  Sender: TObject);
var
  Dir: String;
begin
  Dir := lblEasyWorship.Caption;
  SelectDirectory('Select EasyWorship Directory', '', Dir);
  if not CheckEasyworshipPath(Dir) then
    ShowMessage('The selected directory does not contain a valid EasyWorship Database');

end;

procedure TfrmDatasoulEasyWorshipImportForm.btnCloseClick(Sender: TObject);
begin
  Close;
end;

procedure TfrmDatasoulEasyWorshipImportForm.CreateDatasoulSong(Title, Author, Copyright, Lyrics: String);
var
  i: Integer;
  path, tmp : String;
  outf : TextFile;
  writefile : Boolean;
begin
  path := lblDatasoul.Caption + '\Songs\' + Title + '.song';
  if FileExists(path) and (OverwriteMode <> mrYesToAll) then
    begin
      if OverwriteMode = mrNoToAll then
        writefile := False
      else
        begin
          OverwriteMode := MessageDlg('Song '+Title+' already exists. Overwrite?', mtCustom, [mbYes, mbYesToAll, mbNo, mbNoToAll], 0);
          if (OverwriteMode = mrYes) or (OverwriteMode = mrYesToAll) then
            writefile := True
          else
            writefile := False;
        end;
    end
  else
    writefile := True;

  if WriteFile then
    begin
      AssignFile(outf, path);
      Rewrite(outf);
      for I := 0 to TemplateList.Count - 1 do
        begin
          tmp := TemplateList.Strings[i];
          tmp := StringReplace(tmp, 'DATASOUL_SONG_TITLE', Title, [rfReplaceAll]);
          tmp := StringReplace(tmp, 'DATASOUL_SONG_LYRICS', Lyrics, [rfReplaceAll]);
          tmp := StringReplace(tmp, 'DATASOUL_SONG_AUTHOR', Author, [rfReplaceAll]);
          tmp := StringReplace(tmp, 'DATASOUL_SONG_COPYRIGHT', Copyright, [rfReplaceAll]);
          WriteLn(outf, tmp);
        end;
      CloseFile(outf);
      Inc(ConvertedCount);
    end;
end;

function TfrmDatasoulEasyWorshipImportForm.ConvertTitle(Title: String): String;
begin
  Title := StringReplace(Title, ':', '_',[rfReplaceAll]);
  Title := StringReplace(Title, '"', '_',[rfReplaceAll]);
  Title := StringReplace(Title, '<', '_',[rfReplaceAll]);
  Title := StringReplace(Title, '>', '_',[rfReplaceAll]);
  Title := StringReplace(Title, '/', '_',[rfReplaceAll]);
  Title := StringReplace(Title, '\', '_',[rfReplaceAll]);
  Title := StringReplace(Title, '?', '_',[rfReplaceAll]);
  Title := StringReplace(Title, '*', '_',[rfReplaceAll]);
  Result := Title;
end;

procedure TfrmDatasoulEasyWorshipImportForm.btnGoClick(Sender: TObject);
var
  Title, Lyrics, Author, Copyright : String;
  I: Integer;
begin

  btnChooseEasyWorship.Enabled := False;
  btnChooseDatasoul.Enabled := False;
  btnGo.Enabled := False;

  lblDone.Caption := 'Please wait. Importing...';

  ProgressBar.Max := Table1.RecordCount;
  Table1.First;
  for I := 0 to Table1.RecordCount - 1 do
    begin
      Title := ConvertTitle(Table1.FieldByName('Title').AsString);
      Author := Table1.FieldByName('Author').AsString;
      Copyright := Table1.FieldByName('Copyright').AsString;
      Lyrics := ConvertLyrics(Table1.FieldByName('Words').AsString);
      CreateDatasoulSong(Title, Author, Copyright, Lyrics);
      ProgressBar.Position := I;
      Table1.Next;
    end;

    ProgressBar.Position := ProgressBar.Max;
    lblDone.Caption := 'Done. '+IntToStr(ConvertedCount)+' Songs converted.';

end;

end.
