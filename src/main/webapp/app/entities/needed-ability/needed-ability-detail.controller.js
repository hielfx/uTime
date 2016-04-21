(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('NeededAbilityDetailController', NeededAbilityDetailController);

    NeededAbilityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'NeededAbility', 'Need'];

    function NeededAbilityDetailController($scope, $rootScope, $stateParams, entity, NeededAbility, Need) {
        var vm = this;
        vm.neededAbility = entity;
        vm.load = function (id) {
            NeededAbility.get({id: id}, function(result) {
                vm.neededAbility = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:neededAbilityUpdate', function(event, result) {
            vm.neededAbility = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
