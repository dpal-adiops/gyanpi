MathJax = {
	    mml: {forceReparse: true},
	    startup: {
	      pageReady () {
	        MathJax.startup.defaultPageReady().then(() => {
	          try {
	            console.log (MathJax.tex2svg("x_2 = y^3"))
	          } catch(e) {
	            console.log(e);
	          }
	        });
	      }
	    }
	  }



var quill = new Quill('#editor-container', {
  placeholder: 'Click the formula button to insert a formula.',
  theme: 'snow',
  modules: {
    toolbar: [
        ['bold', 'italic', 'underline', 'strike'],
        ['link'],
        ['blockquote'],
        [{ 'list': 'ordered'}, { 'list': 'bullet' }],
        [{ 'script': 'sub'}, { 'script': 'super' }], 
        ['align', { 'align': 'center' }, { 'align': 'right' }, { 'align': 'justify' }]
    ]
  },
});



//When the MathJax button is clicked, add a mathjax equation at the current selection
document.getElementById('mathjax').onclick = () => {
    var latex = prompt("Enter a LaTeX formula:", "e=mc^2");
    var range = quill.getSelection(true);
    quill.deleteText(range.index, range.length);
    quill.insertEmbed(range.index, 'mathjax', latex);
    quill.insertText(range.index + range.length + 1 , ' ');
    quill.setSelection(range.index + range.length + 1);
}

//Import parchment and delta for creating custom module
const Parchment = Quill.imports.parchment;
const Delta = Quill.imports.delta;

// Extend the embed
class Mathjax extends Parchment.Embed {

    // Create node
    static create(value) 
    {
        const node = super.create(value);    
        if (typeof value === 'string') {
            node.innerHTML = "&#65279;" + this.tex2svg(value) + "&#65279;";
            node.contentEditable = 'false';
            node.setAttribute('data-value', value);         
        }
        return node;
    }

    // Return the attribute value (probably for Delta)
    static value(domNode) 
    {
        return domNode.getAttribute('data-value');
    }

    // Manually render a MathJax equation until version 3.0.2 is not released
    static tex2svg(latex)
    {
        // Create a hidden node and render the formula inside
        let MathJaxNode = document.createElement("DIV");
        MathJaxNode.style.visibility = "hidden";
        MathJaxNode.innerHTML = '\\(' + latex + '\\)';
        document.body.appendChild(MathJaxNode);
        MathJax.typeset();
        let svg = MathJaxNode.innerHTML;
        document.body.removeChild(MathJaxNode);
        return svg;
    }
   
}

// Set module properties
Mathjax.blotName = 'mathjax';
Mathjax.className = 'ql-mathjax';
Mathjax.tagName = 'SPAN';

// Register the module
Quill.register(Mathjax);