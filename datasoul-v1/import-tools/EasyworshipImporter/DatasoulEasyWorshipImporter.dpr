program DatasoulEasyWorshipImporter;

uses
  Forms,
  DatasoulEasyWorshipImportForm in 'DatasoulEasyWorshipImportForm.pas' {frmDatasoulEasyWorshipImportForm};

{$R *.res}

begin
  Application.Initialize;
  Application.Title := 'Datasoul Import Tool for EasyWorship';
  Application.CreateForm(TfrmDatasoulEasyWorshipImportForm, frmDatasoulEasyWorshipImportForm);
  Application.Run;
end.
