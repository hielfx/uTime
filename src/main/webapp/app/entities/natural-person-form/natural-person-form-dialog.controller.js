(function () {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('NaturalPersonFormDialogController', NaturalPersonFormDialogController);

    NaturalPersonFormDialogController.$inject = ['$scope', '$stateParams', 'DataUtils', 'entity', 'NaturalPersonForm', 'Gender'];

    function NaturalPersonFormDialogController($scope, $stateParams, DataUtils, entity, NaturalPersonForm, Gender) {
        var vm = this;
        vm.naturalPersonForm = entity;
        vm.genders = Gender.query();
        vm.load = function (id) {
            NaturalPersonForm.get({id: id}, function (result) {
                vm.naturalPersonForm = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:naturalPersonFormUpdate', result);
            //$uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            NaturalPersonForm.save(vm.naturalPersonForm, onSaveSuccess, onSaveError);

        };

        vm.clear = function () {
            //$uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.birthDate = false;

        vm.setImage = function ($file, naturalPersonForm) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function (base64Data) {
                    $scope.$apply(function () {
                        naturalPersonForm.image = base64Data;
                        naturalPersonForm.imageContentType = $file.type;
                    });
                });
            }
        };

        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        vm.openCalendar = function (date) {
            var _date = new Date();
            var day = _date.getDate();
            var month = _date.getMonth();
            var year = _date.getFullYear();
            var min = _date.getMinutes();
            var sec = _date.getSeconds();

            if (month < 10) {
                month = "0" + month;
            }
            if (day < 10) {
                day = "0" + day;
            }
            var now_date = year + "-" + month + "-" + day + " " + min + ":" + sec;

            if ($("#field_birthDate").val()) {
                _date = $("#field_birthDate").val();
            } else {
                _date = now_date;
            }

            $("#field_birthDate").val(_date).change();
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
