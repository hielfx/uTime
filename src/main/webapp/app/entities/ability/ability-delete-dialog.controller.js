(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('AbilityDeleteController',AbilityDeleteController);

    AbilityDeleteController.$inject = ['$uibModalInstance', 'entity', 'Ability'];

    function AbilityDeleteController($uibModalInstance, entity, Ability) {
        var vm = this;
        vm.ability = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Ability.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
