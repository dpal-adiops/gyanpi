function addEditorForm(){
	 var justHtml = quill.root.innerHTML;
	 document.getElementById("desc").value=justHtml;
	 document.getElementById("editorForm").submit();
}