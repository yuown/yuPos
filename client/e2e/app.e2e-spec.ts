import { YuPosPage } from './app.po';

describe('yu-pos App', function() {
  let page: YuPosPage;

  beforeEach(() => {
    page = new YuPosPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
