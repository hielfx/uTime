(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('NeededAbilityDeleteController',NeededAbilityDeleteController);

    NeededAbilityDeleteController.$inject = ['$uibModalInstance', 'entity', 'NeededAbility'];

    function NeededAbilityDeleteController($uibModalInstance, entity, NeededAbility) {
        var vm = this;
        vm.neededAbility = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            NeededAbility.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
