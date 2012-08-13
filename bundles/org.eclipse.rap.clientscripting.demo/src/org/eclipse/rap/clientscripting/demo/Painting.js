var points = [];

var handleEvent = function( event ) {
  switch( event.type ) {
    case SWT.MouseMove:
      points.push( [ event.x, event.y ] );
      event.widget.redraw();
    break;
    case SWT.Paint:
      event.gc.lineWidth = 4;
      event.gc.beginPath();
      event.gc.moveTo( points[ 0 ][ 0 ], points[ 0 ][ 1 ] );
      for( var i = 1; i < points.length; i++ ) {
        event.gc.lineTo( points[ i ][ 0 ] , points[ i ][ 1 ] );
      }
      event.gc.stroke();
    break;
  }
};
