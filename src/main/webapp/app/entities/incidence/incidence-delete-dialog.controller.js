(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('IncidenceDeleteController',IncidenceDeleteController);

    IncidenceDeleteController.$inject = ['$uibModalInstance', 'entity', 'Incidence'];

    function IncidenceDeleteController($uibModalInstance, entity, Incidence) {
        var vm = this;
        vm.incidence = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Incidence.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
