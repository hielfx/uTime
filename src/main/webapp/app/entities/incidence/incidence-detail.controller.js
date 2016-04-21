(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('IncidenceDetailController', IncidenceDetailController);

    IncidenceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Incidence', 'Administrator', 'Request', 'AppUser'];

    function IncidenceDetailController($scope, $rootScope, $stateParams, entity, Incidence, Administrator, Request, AppUser) {
        var vm = this;
        vm.incidence = entity;
        vm.load = function (id) {
            Incidence.get({id: id}, function(result) {
                vm.incidence = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:incidenceUpdate', function(event, result) {
            vm.incidence = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
