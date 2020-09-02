function addEditorForm(){
	 var justHtml = quill.root.innerHTML;
	 var text=quill.getText()
	 document.getElementById("desc").value=text;
	 document.getElementById("editorForm").submit();
}