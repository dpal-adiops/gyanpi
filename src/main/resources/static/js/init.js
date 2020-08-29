(function($){
  $(function(){

    $('.sidenav').sidenav();

  }); // end of document ready
})(jQuery); // end of jQuery name space


$(document).ready(function(){
    $('select').formSelect();
  
});


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