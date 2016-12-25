'use strict';

describe('Controller Tests', function() {

    describe('Element Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockElement, MockLevelElement;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockElement = jasmine.createSpy('MockElement');
            MockLevelElement = jasmine.createSpy('MockLevelElement');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Element': MockElement,
                'LevelElement': MockLevelElement
            };
            createController = function() {
                $injector.get('$controller')("ElementDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'yuPosApp:elementUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
