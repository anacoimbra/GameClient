'use strict';

angular.module('myApp.desenho', ['ngRoute', 'ngSocket'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/desenho', {
    templateUrl: 'desenho/desenho.html',
    controller: 'DesenhoCtrl'
  });
}])
    .controller('DesenhoCtrl', ['$scope', '$http', '$timeout', 'ChatService', function($scope, $http, $timeout, ChatService) {
      var canvas, ctx,$img, intervalo;
      var ws, stompClient = null;
      $scope.max = 140;

      $scope.desenho = {};
      $scope.desenho.messages = [];

      $scope.trocaTurno = false;

      $scope.jogador = "Esperando iniciar jogo ...";

      $scope.undo = function(){
        $scope.desenho.version --;
      };

      $scope.clean = function(){
        $scope.desenho.version = 0;
      };

      $scope.addMessage = function() {
        ChatService.send($scope.desenho.message);
        $scope.desenho.message = "";
      };

      ChatService.receive().then(null, null, function(message) {
        $scope.desenho.messages.push(message);
      });

      $scope.init = function(){

        $http.get("/jogador")
            .success(function(data){
              $scope.jogador = data;

              if(data.turn){
                $http.get("/palavra")
                    .success(function(data){
                      $scope.desenho.palavra = data;
                    });
              }
            });

        intervalo = setInterval(function () {
          if($scope.jogador.turn) {
            html2canvas($("#cvs"), {
              onrendered: function (canvas) {
                ctx = canvas.getContext('2d');
                $scope.url = canvas.toDataURL("image/png");

                $http.post("/foto",
                    {
                      url: $scope.url
                    }
                );
              },
              width: 300,
              height: 300
            });
          }
          else {
            $http.get("/foto")
                .then(function (data) {
                  console.log(data);
                  if (data.length > 0)
                    $("#img").attr("src");
                  $scope.link = data.data.url;
                });
          }

        }, 1000);
      };

      $scope.stop = function(){
        $scope.clean();
        $scope.trocaTurno = true;
        clearInterval(intervalo);
      }
    }]);
