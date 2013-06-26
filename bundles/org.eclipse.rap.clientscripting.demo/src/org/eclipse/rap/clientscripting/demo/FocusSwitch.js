var handleEvent = function( event ) {
  var text = event.widget;
  var str = text.getText();
  if( str.length === 4 && text.getData( "next" ) ) {
    rap.getObject( text.getData( "next" ) ).forceFocus();
  } else if( str.length === 0 && text.getData( "previous" ) ) {
    rap.getObject( text.getData( "previous" ) ).forceFocus();
  }
};
