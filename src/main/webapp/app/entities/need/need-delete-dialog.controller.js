(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('NeedDeleteController',NeedDeleteController);

    NeedDeleteController.$inject = ['$uibModalInstance', 'entity', 'Need'];

    function NeedDeleteController($uibModalInstance, entity, Need) {
        var vm = this;
        vm.need = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Need.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
