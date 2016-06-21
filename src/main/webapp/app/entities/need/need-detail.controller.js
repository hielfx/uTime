(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('NeedDetailController', NeedDetailController);

    NeedDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Need', 'AppUser', 'NeededAbility', 'Availability', 'Request'];

    function NeedDetailController($scope, $rootScope, $stateParams, entity, Need, AppUser, NeededAbility, Availability, Request) {
        var vm = this;
        vm.need = entity;
        vm.load = function (id) {
            Need.get({id: id}, function(result){
                vm.need = result;
            });
        };

        var unsubscribe = $rootScope.$on('volunteercrowdApp:needUpdate', function(event, result) {
            vm.need = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
