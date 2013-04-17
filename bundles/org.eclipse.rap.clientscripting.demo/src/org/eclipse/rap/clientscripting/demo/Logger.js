var handleEvent = function( event ) {
  if( window.console && console.log ) {
    var info = [ getTypeString( event ) ];
    if( event.widget.getSelection && event.type == SWT.Selection ) {
      info.push( '"' + event.widget.getSelection() + '"' );
    }
    console.log( info.join( " " ) );
  }
};

var getTypeString = function( event ) {
  var result;
  for( var type in SWT ) {
    if( type.charCodeAt( 1 ) >= 97 && SWT[ type ] === event.type ) {
      result = type;
    }
  }
  return result;
};
