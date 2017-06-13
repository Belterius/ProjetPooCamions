var map;
var panel;
var initialize;
var calculate;
var direction;
var points = [];
var route = [];

$('table tr').each(function(){
    var x = $(this).find("td:last").html();
    var y = $(this).find("td").eq(4).html();
    if(x != undefined && y!= undefined)
        route.push(new google.maps.LatLng(x, y));
});

direction = new google.maps.DirectionsRenderer({
    map   : map, 
    panel : panel 
});

calculate = function(){
	var start = route[0];
	var end = route[route.length - 1];
	for(var i = 0; i < route.length - 1; i++){
		points[i] = {location : route[i+1], stopover : true};
	}
        
	var request = {
		origin      : start,
		destination : end,
		travelMode  : google.maps.DirectionsTravelMode.DRIVING, // Type de transport
		waypoints   : points
	}
	var directionsService = new google.maps.DirectionsService(); // Service de calcul d'itinéraire
	directionsService.route(request, function(response, status){ // Envoie de la requête pour calculer le parcours
		while(status != google.maps.DirectionsStatus.OK);
		if(status == google.maps.DirectionsStatus.OK){
			direction.setDirections(response); // Trace l'itinéraire sur la carte et les différentes étapes du parcours
			direction.setMap(map);
			//direction.set("panel",panel);
		}
	});
};

initialize = function(){
  var latLng = route[0];
  var myOptions = {
    zoom      : 14, // Zoom par défaut
    center    : latLng, // Coordonnées de départ de la carte de type latLng 
    mapTypeId : google.maps.MapTypeId.TERRAIN, // Type de carte, différentes valeurs possible HYBRID, ROADMAP, SATELLITE, TERRAIN
    maxZoom   : 20,
    key       : "AIzaSyCwGH4tJhoPRlxgp-7-5kffWc5b_uktoEU"
  };
  
  map      = new google.maps.Map(document.getElementById('map'), myOptions);
  
  var marker = new google.maps.Marker({
    position : latLng,
    map      : map,
    title    : "Lille"
  });
  
  var contentMarker = [
      '<div id="containerTabs">',
      '<div id="tabs">',
      '<ul>',
        '<li><a href="#tab-1"><span>Lorem</span></a></li>',
        '<li><a href="#tab-2"><span>Ipsum</span></a></li>',
        '<li><a href="#tab-3"><span>Dolor</span></a></li>',
      '</ul>',
      '<div id="tab-1">',
        '<h3>Lille</h3><p>Suspendisse quis magna dapibus orci porta varius sed sit amet purus. Ut eu justo dictum elit malesuada facilisis. Proin ipsum ligula, feugiat sed faucibus a, <a href="http://www.google.fr">google</a> sit amet mauris. In sit amet nisi mauris. Aliquam vestibulum quam et ligula pretium suscipit ullamcorper metus accumsan.</p>',
      '</div>',
      '<div id="tab-2">',
       '<h3>Aliquam vestibulum</h3><p>Aliquam vestibulum quam et ligula pretium suscipit ullamcorper metus accumsan.</p>',
      '</div>',
      '<div id="tab-3">',
        '<h3>Pretium suscipit</h3><ul><li>Lorem</li><li>Ipsum</li><li>Dolor</li><li>Amectus</li></ul>',
      '</div>',
      '</div>',
      '</div>'
  ].join('');

  var infoWindow = new google.maps.InfoWindow({
    content  : contentMarker,
    position : latLng
  });
  
  /*google.maps.event.addListener(marker, 'click', function() {
    infoWindow.open(map,marker);
  });
  
  google.maps.event.addListener(infoWindow, 'domready', function(){ // infoWindow est biensûr notre info-bulle
    jQuery("#tabs").tabs();
  });*/
  
  
  google.maps.event.addDomListener(window, 'load', initialize);
  
  calculate();
};

initialize();
