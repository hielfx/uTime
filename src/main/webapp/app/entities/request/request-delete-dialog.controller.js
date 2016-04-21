(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('RequestDeleteController',RequestDeleteController);

    RequestDeleteController.$inject = ['$uibModalInstance', 'entity', 'Request'];

    function RequestDeleteController($uibModalInstance, entity, Request) {
        var vm = this;
        vm.request = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Request.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
