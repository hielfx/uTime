(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('AbilityDetailController', AbilityDetailController);

    AbilityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Ability', 'Tag', 'AppUser'];

    function AbilityDetailController($scope, $rootScope, $stateParams, entity, Ability, Tag, AppUser) {
        var vm = this;
        vm.ability = entity;
        vm.load = function (id) {
            Ability.get({id: id}, function(result) {
                vm.ability = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:abilityUpdate', function(event, result) {
            vm.ability = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
