(function () {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('NaturalPersonFormDeleteController', NaturalPersonFormDeleteController);

    NaturalPersonFormDeleteController.$inject = ['$uibModalInstance', 'entity', 'NaturalPersonForm'];

    function NaturalPersonFormDeleteController($uibModalInstance, entity, NaturalPersonForm) {
        var vm = this;
        vm.naturalPersonForm = entity;
        vm.clear = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            NaturalPersonForm.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
