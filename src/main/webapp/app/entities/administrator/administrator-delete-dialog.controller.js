(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('AdministratorDeleteController',AdministratorDeleteController);

    AdministratorDeleteController.$inject = ['$uibModalInstance', 'entity', 'Administrator'];

    function AdministratorDeleteController($uibModalInstance, entity, Administrator) {
        var vm = this;
        vm.administrator = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Administrator.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
