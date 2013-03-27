var handleEvent = function( event ) {
  if( window.console && console.log ) {
    console.log( event );
    if( event.widget.getSelection && event.type == SWT.Selection ) {
      console.log( event.widget.getSelection() );
    }
  }
};
