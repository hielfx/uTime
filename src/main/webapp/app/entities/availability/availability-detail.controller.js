(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('AvailabilityDetailController', AvailabilityDetailController);

    AvailabilityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Availability', 'Need'];

    function AvailabilityDetailController($scope, $rootScope, $stateParams, entity, Availability, Need) {
        var vm = this;
        vm.availability = entity;
        vm.load = function (id) {
            Availability.get({id: id}, function(result) {
                vm.availability = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:availabilityUpdate', function(event, result) {
            vm.availability = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
