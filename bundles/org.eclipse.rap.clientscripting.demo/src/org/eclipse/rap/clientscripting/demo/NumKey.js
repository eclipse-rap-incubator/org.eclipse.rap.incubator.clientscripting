//# sourceURL=NumKey.js

/*globals demoscripts:true */
demoscripts = ( typeof demoscripts !== "undefined" ) ? demoscripts : {};

demoscripts.numKey = function( event ) {
  var button = event.widget;
  var text = rap.getObject( button.getData( "textWidget" ) );
  var value = button.getData( "numValue" );
  var str = text.getText();
  if( value === -1 ) {
    text.setText( "" );
  } else if( value === -2 ) {
    if( str.length > 0 && str.indexOf( "." ) === -1 ) {
      text.setText( str + "." );
    }
  } else if( value !== 0 || str.length > 0 ){
    text.setText( str + value );
  }
};
