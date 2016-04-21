(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('LegalEntityDeleteController',LegalEntityDeleteController);

    LegalEntityDeleteController.$inject = ['$uibModalInstance', 'entity', 'LegalEntity'];

    function LegalEntityDeleteController($uibModalInstance, entity, LegalEntity) {
        var vm = this;
        vm.legalEntity = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            LegalEntity.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
