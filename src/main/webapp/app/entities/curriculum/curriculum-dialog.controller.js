(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('CurriculumDialogController', CurriculumDialogController);

    CurriculumDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Curriculum', 'NaturalPerson'];

    function CurriculumDialogController ($scope, $stateParams, $uibModalInstance, DataUtils, entity, Curriculum, NaturalPerson) {
        var vm = this;
        vm.curriculum = entity;
        vm.naturalpersons = NaturalPerson.query();
        vm.load = function(id) {
            Curriculum.get({id : id}, function(result) {
                vm.curriculum = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:curriculumUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.curriculum.id !== null) {
                Curriculum.update(vm.curriculum, onSaveSuccess, onSaveError);
            } else {
                Curriculum.save(vm.curriculum, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.creationDate = false;
        vm.datePickerOpenStatus.modificationDate = false;

        vm.setFile = function ($file, curriculum) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        curriculum.file = base64Data;
                        curriculum.fileContentType = $file.type;
                    });
                });
            }
        };

        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
