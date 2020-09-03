function addEditorForm(){
	 var justHtml = quill.root.innerHTML;
	 var text=quill.getText()
	 document.getElementById("description").value=justHtml;
	 document.getElementById("editorForm").submit();
}