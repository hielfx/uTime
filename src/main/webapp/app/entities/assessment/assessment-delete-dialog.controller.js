(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('AssessmentDeleteController',AssessmentDeleteController);

    AssessmentDeleteController.$inject = ['$uibModalInstance', 'entity', 'Assessment'];

    function AssessmentDeleteController($uibModalInstance, entity, Assessment) {
        var vm = this;
        vm.assessment = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Assessment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
