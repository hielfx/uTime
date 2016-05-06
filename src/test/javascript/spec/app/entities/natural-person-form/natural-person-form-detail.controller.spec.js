'use strict';

describe('Controller Tests', function () {

    describe('NaturalPersonForm Management Detail Controller', function () {
        var $scope, $rootScope;
        var MockEntity, MockNaturalPersonForm, MockGender;
        var createController;

        beforeEach(inject(function ($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockNaturalPersonForm = jasmine.createSpy('MockNaturalPersonForm');
            MockGender = jasmine.createSpy('MockGender');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'NaturalPersonForm': MockNaturalPersonForm,
                'Gender': MockGender
            };
            createController = function () {
                $injector.get('$controller')("NaturalPersonFormDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function () {
            it('Unregisters root scope listener upon scope destruction', function () {
                var eventType = 'volunteercrowdApp:naturalPersonFormUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
