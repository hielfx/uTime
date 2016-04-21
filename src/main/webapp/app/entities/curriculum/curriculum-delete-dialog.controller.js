(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('CurriculumDeleteController',CurriculumDeleteController);

    CurriculumDeleteController.$inject = ['$uibModalInstance', 'entity', 'Curriculum'];

    function CurriculumDeleteController($uibModalInstance, entity, Curriculum) {
        var vm = this;
        vm.curriculum = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Curriculum.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
