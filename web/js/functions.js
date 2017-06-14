var map;
var panel;
var initialize;
var calculate;
var direction;
var points = [];
var route = [];

$('table tr').each(function () {
    var x = $(this).find("td:last").html();
    var y = $(this).find("td").eq(4).html();
    if (x != undefined && y != undefined)
        route.push(new google.maps.LatLng(x, y));
});

direction = new google.maps.DirectionsRenderer({
    map: map,
    panel: panel,
    suppressMarkers: true
});

calculate = function () {
    var start = route[0];
    var end = route[route.length - 1];
    for (var i = 0; i < route.length - 1; i++) {
        points[i] = {location: route[i + 1], stopover: true};
    }

    var request = {
        origin: start,
        destination: end,
        travelMode: google.maps.DirectionsTravelMode.DRIVING, // Type de transport
        waypoints: points
    }
    var directionsService = new google.maps.DirectionsService(); // Service de calcul d'itinéraire
    directionsService.route(request, function (response, status) { // Envoie de la requête pour calculer le parcours
        while (status != google.maps.DirectionsStatus.OK)
            ;
        if (status == google.maps.DirectionsStatus.OK) {
            direction.setDirections(response); // Trace l'itinéraire sur la carte et les différentes étapes du parcours
            for (var j = 0; j < route.length - 1; j++) {
                if (j == 0) {
                    var image = 'https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png';
                }
                else{
                    var image = null;
                }

                var label = $("table tr").eq(j+1).children().eq(1).html();
                

                var marker = new google.maps.Marker({
                    position: route[j + 1],
                    map: map,
                    label: label,
                    icon: image
                });

                marker.addListener('click', function (){ 
                    $('table > tbody  > tr').each(function() {
                        $(this).css('background-color', '');
                        $(this).css('box-shadow', '')
                    });
                    var ligneTabAsso = $("#" + this.label);
                    ligneTabAsso.css('background-color', '#a8cb17');
                    ligneTabAsso.css('box-shadow', '0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19)')
                });
            }
            direction.setMap(map);
            //direction.set("panel",panel);
        }
    });
};

initialize = function () {
    var latLng = route[0];
    var myOptions = {
        zoom: 14, // Zoom par défaut
        center: latLng, // Coordonnées de départ de la carte de type latLng 
        mapTypeId: google.maps.MapTypeId.ROADMAP, // Type de carte, différentes valeurs possible HYBRID, ROADMAP, SATELLITE, TERRAIN
        maxZoom: 20,
        key: "AIzaSyCwGH4tJhoPRlxgp-7-5kffWc5b_uktoEU"
    };

    map = new google.maps.Map(document.getElementById('map'), myOptions);

    /*var marker = new google.maps.Marker({
     position: latLng,
     map: map,
     title: "Lille"
     });*/

    var contentMarker = [
        '<div id="containerTabs">',
        '<div id="tabs">',
        '',
        '</div>',
        '</div>'
    ].join('');

    var infoWindow = new google.maps.InfoWindow({
        content: contentMarker,
        position: latLng
    });

    /*google.maps.event.addListener(marker, 'click', function () {
     infoWindow.open(map, marker);
     });*/

    /*google.maps.event.addListener(infoWindow, 'domready', function(){ // infoWindow est biensûr notre info-bulle
     jQuery("#tabs").tabs();
     });*/


    google.maps.event.addDomListener(window, 'load', initialize);

    google.maps.event.addListener(map, 'zoom_changed', function () {
        setTimeout(function () {
            var cnt = map.getCenter();
            cnt.e += 0.000001;
            map.panTo(cnt);
            cnt.e -= 0.000001;
            map.panTo(cnt);
        }, 400);
    });

    calculate();
};

initialize();